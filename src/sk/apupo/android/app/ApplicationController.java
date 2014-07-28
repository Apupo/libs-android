package sk.apupo.android.app;

import sk.apupo.android.dao.DaoController;
import android.app.Application;

public class ApplicationController extends Application {
	
	//public final Logger logger = LoggerFactory.getLogger();
	
	private DaoController daoController;
	public DaoController getDaoController() { return this.daoController; }

	public ApplicationController() {
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		if(this.daoController == null) {
			this.daoController = instantiateDaoController();
		}
	}

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    
    protected DaoController instantiateDaoController() {
    	return new DaoController(this, null, null, null);
    }
}
