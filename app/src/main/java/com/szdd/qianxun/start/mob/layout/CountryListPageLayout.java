package com.szdd.qianxun.start.mob.layout;

import android.content.Context;
import android.widget.LinearLayout;

import com.szdd.qianxun.start.mob.CountryListView;

/** 国家列表页面布局 */
public class CountryListPageLayout extends BasePageLayout {

	public CountryListPageLayout(Context c) {
		super(c, true);
	}

	protected void onCreateContent(LinearLayout parent) {
		CountryListView countryList = new CountryListView(context);
		countryList.setId(Res.id.clCountry);
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		countryList.setLayoutParams(listParams);

		parent.addView(countryList);
	}

}
