/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.report.chart.flash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import ofc4j.model.Chart;
import ofc4j.model.axis.XAxis;
import ofc4j.model.axis.YAxis;
import ofc4j.model.elements.HorizontalBarChart;

/**
 * Created on Mar 18, 2009, 12:51:28 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class HorizontalChartBuilder extends ReportChartFlashBuilder
{
	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.chart.flash.ReportChartFlashBuilder#build(net.rrm.ehour.report.reports.ReportData, net.rrm.ehour.ui.report.chart.AggregateChartDataConvertor)
	 */
	@Override
	protected void build(Map<ChartRowKey, Number> valueMap, Chart chartContainer)
	{
		HorizontalBarChart chart = new HorizontalBarChart();
		
		List<ChartRowKey> keys = new ArrayList<ChartRowKey>(valueMap.keySet());
		Collections.sort(keys);
		
		List<String> labels = new ArrayList<String>();
		
		for (ChartRowKey rowKeyAgg : keys)
		{
			labels.add((String) rowKeyAgg.getName());
			Number value = valueMap.get(rowKeyAgg);
		
			chart.addBar(0, value);
		}

		chartContainer.addElements(chart);
		
		YAxis yAxis = new YAxis()
						.addLabels(labels)
						.setTickLength(15);
		
		chartContainer.setXAxis(new XAxis().setTickHeight(1));
		chartContainer.setYAxis(yAxis);
		
//		chartContainer.getXAxis().setTickHeight(5);
	}
}
