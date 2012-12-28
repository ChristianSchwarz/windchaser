package org.stormchaser.client;

import java.util.Arrays;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Wind_Chaser implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final Label label = new Label();

		RootPanel.get("errorLabelContainer").add(label);

		RepeatingCommand repeatingCommand = new RepeatingCommand() {

			@Override
			public boolean execute() {
				greetingService.greetServer(new AsyncCallback<int[]>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						label.setText("Remote Procedure Call - Failure:" + caught);
						label.addStyleName("serverResponseLabelError");

					}

					@Override
					public void onSuccess(int[] result) {
						label.setText(Arrays.toString(result));
						label.removeStyleName("serverResponseLabelError");
					}
				});
				return true;
			}
		};
		Scheduler.get().scheduleFixedDelay(repeatingCommand, 500);

	}
}
