package com.cdeducation.model;

import com.cdeducation.data.NewsItems;

import java.util.ArrayList;
import java.util.List;

/**
 * @author t77yq @2014-07-18.
 */
public class NewsItemsModel extends ModelObservable {

	private List<NewsItems> nItems;

	public NewsItemsModel() {
		this.nItems = new ArrayList<NewsItems>();
	}
	public void createNItems(List<NewsItems> nItems) {
		if (nItems != null) {
			if (this.nItems != null)
				this.nItems=nItems;
			else
				this.nItems = new ArrayList<NewsItems>();
		}
		notifyDataSetChanged();
	}
	public void setNItem(List<NewsItems> nItems) {
		if (nItems != null) {
			if (this.nItems != null)
				this.nItems.addAll(nItems);
			else
				this.nItems = new ArrayList<NewsItems>();
		}
		notifyDataSetChanged();
	}

	public void clear() {
		if (this.nItems != null) {
			nItems.clear();
		}
	}

	public void addNItem(NewsItems nItem) {
		if (nItem != null) {
			this.nItems.add(nItem);
		}
		notifyDataSetChanged();
	}

	public int size() {
		return nItems != null ? nItems.size() : 0;
	}

	public List<NewsItems> getNItems() {
		return nItems;
	}

	public NewsItems getNItem(int index) {
		return index >= 0 ? getNItems().get(index) : null;
	}
}
