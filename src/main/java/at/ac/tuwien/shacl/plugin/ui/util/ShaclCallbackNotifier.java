package at.ac.tuwien.shacl.plugin.ui.util;

import java.util.HashSet;
import java.util.Set;

public class  ShaclCallbackNotifier {
	//private static Set<ShaclCallbackListener> listeners = new HashSet<ShaclCallbackListener>();
	private static ShaclCallbackListener listener;
	
	public static void notify(String message) {
		listener.handleMessage(message);
	}

	public static void setListener(ShaclCallbackListener listener_) {
		listener = listener_;
	}
}
