package at.ac.tuwien.shacl.plugin.ui.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class  ShaclCallbackNotifier {
	private static List<ShaclCallbackListener> listeners = new ArrayList<ShaclCallbackListener>();
	
	public static void notify(String message) {
		for(ShaclCallbackListener l : listeners) {
			l.handleMessage(message);
		}
	}

	public static void register(ShaclCallbackListener listener) {
		listeners.add(listener);
	}
}
