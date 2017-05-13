package com.szdd.qianxun.start.mob.layout;

import android.content.Context;
import android.widget.LinearLayout;

import com.szdd.qianxun.start.mob.ContactsListView;

/** 联系人列表页面布局 */
public class ContactListPageLayout extends BasePageLayout {

	public ContactListPageLayout(Context c) {
		super(c, true);
	}

	protected void onCreateContent(LinearLayout parent) {
		ContactsListView contactsList = new ContactsListView(context);
		contactsList.setId(Res.id.clContact);
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		contactsList.setLayoutParams(listParams);

		parent.addView(contactsList);
	}

}
