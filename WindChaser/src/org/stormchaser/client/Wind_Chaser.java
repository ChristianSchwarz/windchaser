package org.stormchaser.client;

import java.util.Arrays;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Wind_Chaser implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private DataTable data;

	private LineChart atl;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		Runnable onLoadCallback = new Runnable() {

			public void run() {
				data = DataTable.create();
				data.addColumn(AbstractDataTable.ColumnType.NUMBER, "Date");
				data.addColumn(AbstractDataTable.ColumnType.NUMBER, "diff");
				

				Options options = Options.create();
			    options.setHeight(240);
			    options.setTitle("Company Performance");
			    options.setWidth(400);
			    options.setInterpolateNulls(true);
			    AxisOptions vAxisOptions = AxisOptions.create();
			    vAxisOptions.setMinValue(0);
			    vAxisOptions.setMaxValue(2000);
			    options.setVAxisOptions(vAxisOptions);
			   
				
				atl = new LineChart(data, options);
				RootPanel.get().add(atl);

				final RootPanel rootPanel = RootPanel.get("rootDiv");

				final Label label = new Label();
				rootPanel.add(label);

				RepeatingCommand repeatingCommand = updateDiffs(label);
				Scheduler.get().scheduleFixedDelay(repeatingCommand, 500);
			}

		};
		VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);

	}

	private RepeatingCommand updateDiffs(final Label label) {
		return new RepeatingCommand() {

			@Override
			public boolean execute() {
				int x=0;
				greetingService.greetServer(new AsyncCallback<int[]>() {

					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						label.setText("Remote Procedure Call - Failure:" + caught);
						label.addStyleName("serverResponseLabelError");

					}

					@Override
					public void onSuccess(int[] result) {
						if (result == null)
							return; // no new data
						label.setText(Arrays.toString(result));
						label.removeStyleName("serverResponseLabelError");

						
						
						for (int i = 0; i < result.length; i++) {
							float value = Math.max(0, 1000/(result[i]/100));
							int row = data.addRow();
							
							data.setValue(row, 1, value);
							data.setValue(row, 0, row);

						}

						atl.draw(data);

					}
				});
				return true;
			}
		};
	}
}
