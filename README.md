# 基于aliyun mq服务的spring boot starter

### 用法：
1. pom.xml引入依赖，当前version：1.0.0-SNAPSHOT

         <dependency>
            <groupId>com.ieyecloud</groupId>
            <artifactId>mq-spring-boot-starter</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
		
2. application配置文件中添加相应配置

	    aliyun:
  			mq:
    			onsAddr: http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet
    			topic: mulin_topic_test
    			accessKey: xxx
    			secretKey: xxx
    			producer:
      				enabled: true  #为false表示不引入producer，为true则producerId必须提供
      				producerId: xxx
    			consumer:
      				enabled: true  #为false表示不引入consumer，为true则consumerId必须提供
      			consumerId: xxx

3. 使用producer，consumer只需要在相应类中依需要注入对应实例
		
		@Autowired
    	private MqTimerProducer producer;

    	@Autowired
    	private MqConsumer consumer;
	
4. consumer监听处理类实现，继承AbstractMessageListener类，实现handle方法即可，如

		
		@Component
		public class QuestionStatusMessageListener extends AbstractMessageListener{

		    @Autowired
		    private QuickQuestionService questionService;

		    @Override
		    public void handle(String s) {
		        QuestionStatusMessage message = JsonUtil.fromJson(s, QuestionStatusMessage.class);
		        questionService.updateStatus(message.getQid(), message.getCs(), message.getTs());
		    }
		}
