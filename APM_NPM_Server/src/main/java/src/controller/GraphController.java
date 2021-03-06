package src.controller;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import src.globalinfo.ResponseCode;
import src.graphutil.GraphUtil;

import src.mode.ResponseMessage;
/*
 * 从solr中查询出数据
 */
@Controller
public class GraphController {
		
		@Autowired
		private GraphUtil graphUtil;
		
		private static Logger log = LoggerFactory.getLogger(GraphController.class);
		
		@RequestMapping(value="/graphJsonData",method=RequestMethod.GET)  
	    @ResponseBody 
		public ResponseMessage getGraph(@RequestParam String startTime,@RequestParam String endTime){
			ResponseMessage responseMessage = new ResponseMessage();
			/*if(startTime <0 || endTime <0 || startTime>= endTime){
				responseMessage.setCode(ResponseCode.FAIL.getCode());
				responseMessage.setData("时间错误");
			}else{*/
				try {
					String graphStr = graphUtil.getGraphByTime(startTime, endTime);
					responseMessage.setCode(ResponseCode.SUCCESS.getCode());
					responseMessage.setData(graphStr);
					FileWriter writer;
			        try {
			            writer = new FileWriter("/Users/zyd/test.json");
			            writer.write(graphStr);
			            writer.flush();
			            writer.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
				} catch (SolrServerException e) {
					log.error("获取指定时间段的拓扑图数据错误", e);
					responseMessage.setCode(ResponseCode.FAIL.getCode());
					responseMessage.setData("获取指定时间段的拓扑图数据错误");
				}
		//	}
			return responseMessage;
		}
		
}
