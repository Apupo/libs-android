package sk.apupo.android.dao;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sk.apupo.android.utils.ObjectReflectionUtil;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import de.greenrobot.dao.AbstractDao;

public class DaoController {
	
	public static boolean DEBUG								=						true;
	
	private Object helper;
	private Object writableSession;
	
	protected Class<?> devOpenHelperClass;
	protected Class<?> daoMasterClass;
	protected Class<?> daoSessionClass;
	
	private String databaseName;
	
	public String getDatabaseName() { return this.databaseName; }
	
	public String getDatabasePath() {
		if(DEBUG) return this.context.getExternalFilesDir(null) + File.separator + "databases" + File.separator;
		else return this.context.getFilesDir() + File.separator + "databases" + File.separator;
	}
	
	private Context context;
	public Context getContext() { return this.context; }
	
	public DaoController(Context context, String databaseName, Class<?> devOpenHelperClass, Class<?> daoMasterClass, Class<?> daoSessionClass) {
		this.context = context;
		this.databaseName = databaseName;
		this.devOpenHelperClass = devOpenHelperClass;
		this.daoMasterClass = daoMasterClass;
		this.daoSessionClass = daoSessionClass;

		initializeHelper();
	}
	
	private Object getWritableSession() {
		if (this.writableSession == null) {
			Method method;
			SQLiteDatabase db;
			Constructor<?> constructor;
			Object daoMaster = null;
			try {
				method = ObjectReflectionUtil.getMethod(this.helper.getClass(), "getWritableDatabase", null);
				db = (SQLiteDatabase) method.invoke(this.helper, (Object[]) null);
				constructor = this.daoMasterClass.getConstructor(SQLiteDatabase.class);
				daoMaster = constructor.newInstance(db);
				method = ObjectReflectionUtil.getMethod(daoMasterClass, "newSession", null);
				this.writableSession = method.invoke(daoMaster, (Object[]) null);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}

		return this.writableSession;
	}
	
	public AbstractDao<?, ?> getDao(Class<?> cls) {
		String function = "get"+cls.getSimpleName();
		Method method = null;
		try {
			method = ObjectReflectionUtil.getMethod(this.daoSessionClass, function, null);
			return (AbstractDao<?, ?>) method.invoke(this.getWritableSession(), (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected boolean initializeHelper() {
		try {
			Class<?>[] args = new Class[] {Context.class, String.class, CursorFactory.class};
			Constructor<?> constructor = this.devOpenHelperClass.getConstructor(args);
			Object[] initArgs = new Object[] {getContext(), getDatabaseFile().getAbsolutePath(), null};
			this.helper = constructor.newInstance(initArgs);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return this.helper != null ? true : false;
	}
	
	protected boolean isDatabaseFileExists() {
		return getDatabaseFile().exists();
	}
	
	protected File getDatabaseFile() {
		return new File(getDatabasePath() + getDatabaseName());
	}
}
