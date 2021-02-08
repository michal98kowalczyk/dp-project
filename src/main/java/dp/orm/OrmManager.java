package dp.orm;

import dp.orm.exceptions.GetDaoException;
import lombok.extern.apachecommons.CommonsLog;

import java.util.HashMap;
import java.util.Map;

@CommonsLog
public class OrmManager {

    private static Map<Class<?>, Dao<?>> daoMap = new HashMap<>();

   public static void addDao(Class<?> cls, Dao<?> dao){
        daoMap.put(cls,dao);
    }

    public static <T> Dao<T> getDao(Class<T> cls){
       if (daoMap.containsKey(cls)) return (Dao<T>) daoMap.get(cls);

       throw new GetDaoException("Error during getting dao for " + cls.getName());
    }
 }
