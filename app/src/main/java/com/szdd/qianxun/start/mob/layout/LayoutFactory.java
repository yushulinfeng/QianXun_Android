package com.szdd.qianxun.start.mob.layout;

import android.content.Context;
import android.view.ViewGroup;

/** 布局工厂 */
public class LayoutFactory {

	static ViewGroup create(Context context, String name) {
		ViewGroup v = null;
		if (name.equals("smssdk_back_verify_dialog")) {
			v = BackVerifyDialogLayout.create(context);
		} else if (name.equals("smssdk_contact_detail_page")) {
			ContactDetailPageLayout page = new ContactDetailPageLayout(context);
			v = page.getLayout();
		} else if (name.equals("smssdk_contact_list_page")) {
			ContactListPageLayout page = new ContactListPageLayout(context);
			v = page.getLayout();
		} else if (name.equals("smssdk_contacts_listview_item")) {
			v = ContactsListviewItemLayout.create(context);
		} else if (name.equals("smssdk_country_list_page")) {
			CountryListPageLayout page = new CountryListPageLayout(context);
			v = page.getLayout();
		} else if (name.equals("smssdk_input_identify_num_page")) {
			IdentifyNumPageLayout page = new IdentifyNumPageLayout(context);
			v = page.getLayout();
		} else if (name.equals("smssdk_progress_dialog")) {
			v = ProgressDialogLayout.create(context);
		} else if (name.equals("smssdk_register_page")) {
			RegisterPageLayout layout = new RegisterPageLayout(context);
			v = layout.getLayout();
		} else if (name.equals("smssdk_send_msg_dialog")) {
			v = SendMsgDialogLayout.create(context);
		}

		return v;
	}

}
