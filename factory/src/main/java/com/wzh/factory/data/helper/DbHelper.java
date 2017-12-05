package com.wzh.factory.data.helper;


import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.wzh.factory.model.db.AppDatabase;
import com.wzh.factory.model.db.Group;
import com.wzh.factory.model.db.GroupMember;
import com.wzh.factory.model.db.Group_Table;
import com.wzh.factory.model.db.Message;
import com.wzh.factory.model.db.Session;
import com.wzh.utils.CollectionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库的辅助工具类  辅助完成：增删改
 * Created by HP on 2017/11/30.
 *
 * @author by wangWei
 */

public class DbHelper {

    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {
    }

    /**
     * 观察者的集合
     * Class<?>:观察者的表
     * set<ChangedListener>:每一个表对应的观察者有很多
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 从所有的监听者中，获取某一个表的所有监听者
     *
     * @param modelClass 表对应的Class信息
     * @param <Model>    范型
     * @return Set<ChangedListener>
     */
    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass) {
        if (changedListeners.containsKey(modelClass)) {
            return changedListeners.get(modelClass);
        }
        return null;

    }


    /**
     * 添加一个监听
     *
     * @param tClass   对某个表关注
     * @param listener 监听者
     * @param <Model>  表的泛型
     */
    public static <Model extends BaseModel> void addChangedListener(Class<Model> tClass,
                                                                    ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            changedListeners = new HashSet<>();
            instance.changedListeners.put(tClass, changedListeners);
        }
        changedListeners.add(listener);

    }

    /**
     * 删除某一个表的某一个监听器
     *
     * @param tClass   对某个表关注
     * @param listener 监听者
     * @param <Model>  表的泛型
     */
    public static <Model extends BaseModel> void removeChangedListener(Class<Model> tClass,
                                                                       ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            return;
        }
        // 从容器中删除你这个监听者
        changedListeners.remove(listener);

    }


    /**
     * 新增或者修改的统一方法
     *
     * @param tClass 传递一个Class信息
     * @param models 这个Class对应的实例的数组
     * @param <T>    这个实例的范型，限定条件是BaseModel
     */
    public static <T extends BaseModel> void save(final Class<T> tClass, final T... models) {
        if (models == null || models.length == 0) {
            return;
        }
        //当前数据库的管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {

                //执行
                ModelAdapter<T> adapter = FlowManager.getModelAdapter(tClass);
                //保存
                adapter.saveAll(Arrays.asList(models));
                //唤起通知
                instance.notifySave(tClass, models);

            }
        }).build().execute();

    }


    /**
     * 进行删除数据库的统一封装方法
     *
     * @param tClass 传递一个Class信息
     * @param models 这个Class对应的实例的数组
     * @param <T>    这个实例的范型，限定条件是BaseModel
     */
    public static <T extends BaseModel> void delete(final Class<T> tClass,
                                                    final T... models) {
        if (models == null || models.length == 0) {
            return;
        }

        // 当前数据库的一个管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        // 提交一个事物
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // 执行
                ModelAdapter<T> adapter = FlowManager.getModelAdapter(tClass);
                // 删除
                adapter.deleteAll(Arrays.asList(models));
                // 唤起通知
                instance.notifyDelete(tClass, models);
            }
        }).build().execute();
    }


    /**
     * 执行通知调用
     *
     * @param tClass 通知类型
     * @param models 通知的数组
     * @param <T>    实例的泛型 限定条件是BaseModel
     */
    private <T extends BaseModel> void notifySave(Class<T> tClass, T... models) {
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<T> listener : listeners) {
                listener.onDataSave(models);
            }
        }

        //例外情况
        if (GroupMember.class.equals(tClass)) {
            //群成员变更，需要通知对应群信息更新
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            //消息变化，应该通知消息会话列表更新
            updateSession((Message[]) models);

        }


    }


    /**
     * 进行通知调用
     *
     * @param tClass 通知的类型
     * @param models 通知的Model数组
     * @param <T>    这个实例的范型，限定条件是BaseModel
     */
    @SuppressWarnings("unchecked")
    private final <T extends BaseModel> void notifyDelete(final Class<T> tClass,
                                                          final T... models) {
        // 找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            // 通用的通知
            for (ChangedListener<T> listener : listeners) {
                listener.onDataDelete(models);
            }
        }


        if (GroupMember.class.equals(tClass)) {

            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {     // 消息变化，应该通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 从成员中找出找出成员对应的群，并对群进行更新
     *
     * @param members
     */
    private void updateGroup(GroupMember... members) {
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            groupIds.add(member.getGroup().getId());
        }
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //发起二次通知
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();
                instance.notifySave(Group.class, CollectionUtil.toArray(groups, Group.class));


            }
        }).build().execute();


    }

    /**
     * 从消息列表中，筛选出对应的会话，并对会话进行更新
     *
     * @param messages Message列表
     */
    private void updateSession(Message... messages) {
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identifies.add(identify);
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];

                int index = 0;
                for (Session.Identify identify : identifies) {
                    Session session = SessionHelper.findFromLocal(identify.id);

                    if (session == null) {
                        // 第一次聊天，创建一个你和对方的一个会话
                        session = new Session(identify);
                    }

                    // 把会话，刷新到当前Message的最新状态
                    session.refreshToNow();
                    adapter.save(session);
                    sessions[index++] = session;
                }

                instance.notifySave(Session.class, sessions);

            }
        }).build().execute();
    }

    /**
     * 通知监听器
     *
     * @param <Data>
     */
    public interface ChangedListener<Data extends BaseModel> {
        void onDataSave(Data... list);

        void onDataDelete(Data... list);
    }


}
