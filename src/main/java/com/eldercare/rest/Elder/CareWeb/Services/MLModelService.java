package com.eldercare.rest.Elder.CareWeb.Services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MLModelService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MLModelService.class);

	public Map predictStrokeLevel(Map payload) throws Exception {
		Evaluator evaluator = new LoadingModelEvaluatorBuilder()
				.load(new File("model.pmml"))
				.build();
		LOGGER.info(evaluator.getInputFields().toString());
		Map result=evaluator.evaluate(payload);
		LOGGER.info(result.toString());
		return result;
	}
}
