package sk.apupo.android.adapters;

import java.util.ArrayList;
import java.util.Observable;

import sk.apupo.android.interfaces.ObserverBase;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;


public class ObservableBaseAdapter extends BaseAdapter {

	private ArrayList<Observable> mObservableDataList;
	private AbsListView mAbsListView;
	
	private Context mContext;
	public Context getContext() { return mContext; }
	
	public ObservableBaseAdapter(Context context, AbsListView absListView, ArrayList<Observable> observableDataList) {
		mContext = context;
		mAbsListView = absListView;
		mObservableDataList = observableDataList;
		
		mAbsListView.setRecyclerListener(new AbsListView.RecyclerListener() {
			
			@Override
			public void onMovedToScrapHeap(View view) {
				if(view != null && view instanceof ObserverBase) {
					((ObserverBase) view).getData().deleteObserver(((ObserverBase) view));
				}
			}
		});
	}
	
	@Override
	public int getCount() {
		return mObservableDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mObservableDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ObserverBase rendererView = (ObserverBase) convertView;
		
		if(rendererView == null) rendererView = instantiateObserverView();
		
		if(rendererView != null) rendererView.setData(mObservableDataList.get(position));
		
		return (View) rendererView;
	}
	
	protected ObserverBase instantiateObserverView() {
		return null;
	}
}
