package de.mfz.coordinator;

import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;

public class RouteChangedCoordinator implements ItemEventListener {

	@Override
	public void handlePublishedItems(ItemPublishEvent route) {
        System.out.println(route.getNodeId());
	}

}
