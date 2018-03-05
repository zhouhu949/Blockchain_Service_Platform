package com.abs.exception;

public enum ExceptionEnum {
    SUCCESS("CF0000","成功"),
    UNKNOWN("CF9999","未知异常"),
    
	/** 请求参数异常，格式为 "CF10XX" */
	REQUEST_PARAMETER_ERROR("CF1001", "请求参数错误"),
	REQUEST_PARAMETER_FORMAT_ERROR("CF1002", "请求参数格式错误"),
	REQUEST_PARAMETER_NULL_ERROR("CF1003", "请求参数为空"),	
	REQUEST_PARAMETER_ASSETUID_NOT_FOUND("CF1010", "请求参数错误：未找到assetUid"),
	REQUEST_PARAMETER_TXTYPE_NOT_FOUND("CF10011", "请求参数错误：字段txType错误"),	
	REQUEST_PARAMETER_CONFIRM_RESULT_ERROR("CF1012", "请求参数错误：字段确认结果（confirmResult）非法"),
	REQUEST_PARAMETER_CURRENT_ISSUE_ERROR("CF1013", "请求参数错误：字段本期期数（currentIssue）非法"),
	REQUEST_PARAMETER_BUYBACK_STATUS_ERROR("CF1014", "请求参数错误：字段回购确认状态（buybackStatus）非法"),
	REQUEST_PARAMETER_BUYBACK_CATEGORY_ERROR("CF1015", "请求参数错误：字段回购分类（buybackCategory）非法"),	
	REQUEST_PARAMETER_APPROVAL_RESULT_ERROR("CF1016", "请求参数错误：字段审批结果（approveResult）非法"),
	REQUEST_PARAMETER_INTERVAL_VALUE_ERROR("CF10017", "请求参数错误：字段interval取值错误"),	
    REQUEST_PARAMETER_ASSETUID_LENGTH_ERROR("CF1018", "请求参数错误：字段assetUid长度错误"),
    REQUEST_PARAMETER_OUTTRADENO_LENGTH_ERROR("CF1019", "请求参数错误：字段outTradeNo长度错误"),
    REQUEST_PARAMETER_TIMESTAMP_LENGTH_ERROR("CF10020", "请求参数错误：字段timestamp长度错误"),
    REQUEST_PARAMETER_ORGCODE_LENGTH_ERROR("CF10021", "请求参数错误：字段orgCode长度错误"),	
    REQUEST_PARAMETER_BLOCK_HASH_LENGTH_ERROR("CF10022", "请求参数错误：字段blockHash长度错误"),	
    REQUEST_PARAMETER_TX_ID_LENGTH_ERROR("CF10023", "请求参数错误：字段txId长度错误"),	
    REQUEST_PARAMETER_TIME_START_FORMAT_ERROR("CF10024", "请求参数错误：字段timeStart格式（yyyyMMddHHmmss）错误"),	
    REQUEST_PARAMETER_TIME_END_FORMAT_ERROR("CF10025", "请求参数错误：字段timeEnd格式（yyyyMMddHHmmss）错误"),	
    REQUEST_PARAMETER_TIME_END_BEFORE_START_ERROR("CF10026", "请求参数错误：结束日期小于开始日期"),	
    REQUEST_PARAMETER_TIME_END_AFTER_NOW_ERROR("CF10027", "请求参数错误：结束日期大于当前日期"),	
    REQUEST_PARAMETER_TIME_END_EQ_START_ERROR("CF10028", "请求参数错误：结束日期等于当前日期"),  
    REQUEST_PARAMETER_BLOCK_HEIGHT_ERROR("CF10029", "请求参数错误：blockHeight格式错误"),  
	
	/** 查询相关异常，格式为 "CF11XX"，以 QUERY 开头 */
	QUERY_RESULT_EXCEED_100_ITEMS("CF1101","查询错误：查询结果超过100条"),
	QUERY_BLOCKHASH_INVALID("CF1102","查询错误：blockHash无效"),
	QUERY_TXID_INVALID("CF1103","查询错误：txId无效"),
	
	/** 证书、私钥相关异常，格式为 "CF12XX"，以 FABRIC_USER_KEY 或  FABRIC_USER_CERT 开头*/
	BLOCKCHAIN_USER_KEY_PARSE_ERROR("CF1201", "区块链系统错误：用户私钥读取错误"),
	BLOCKCHAIN_USER_CERT_PARSE_ERROR("CF1202", "区块链系统错误：用户证书读取错误"),
	BLOCKCHAIN_USER_KEY_CERT_MISMATCH("CF1203", "区块链系统错误：用户私钥与证书不匹配"),
	
	/** 交易封装异常，格式为 "CF13XX"，以 TX 开头 */
	TX_DUPLICATE_ERROR("CF1301", "交易入链错误：交易重复发送"),
	TX_BUSINESS_FLOW_ERROR("CF1302", "交易入链错误：业务流程错误"),
	TX_PARAMETER_INCOMPLETE("CF1303", "交易入链错误：交易参数不完整"),
	
	/** Chaincode执行相关异常，格式为 "CF14XX"，以 CHAINCODE_EXEC 开头 */
	CHAINCODE_EXEC_PARAMETERS_NUMBER_INCORRECT("CF1401", "智能合约执行错误：参数个数错误"),
	CHAINCODE_EXEC_PUT_STATE_ERROR("CF1402", "智能合约执行错误：状态入链错误"),
	CHAINCODE_EXEC_GET_STATE_ERROR("CF1403", "智能合约执行错误：状态查询错误"),
	CHAINCODE_EXEC_OPERATION_ERROR("CF1404", "智能合约执行错误：基础操作错误"),
	CHAINCODE_EXEC_NO_STAGE_DEFINITION("CF1405", "智能合约执行错误：没有找到业务状态定义"),
	CHAINCODE_EXEC_CATEGORY_INCORRECT("CF1406", "智能合约执行错误：业务类别错误"),
	CHAINCODE_EXEC_NO_WRITE_ACCESS("CF1407", "智能合约执行错误：写入权限校验错误"),
	CHAINCODE_EXEC_INCORRECT_SEQUENCE("CF1408", "智能合约执行错误：业务流程校验错误"),
	CHAINCODE_EXEC_UNKNOW_ERROR("CF1409", "智能合约执行错误：未知执行异常"),
	
	/** 区块解析异常，格式为 "CF15XX"，以 FABRIC_BLOCK 开头 */
	BLOCK_HASH_COMPUTE_ERROR("CF1501", "区块异常：区块哈希计算错误"),
	
	/** 系统异常，格式为 "CF2XXX" */	
	SYSTEM_ERROR("CF2000", "系统错误"),
	
	/** 系统配置异常，格式为 "CF21XX"，以 CONFIG 开头 */
	CONFIG_FILE_NOT_EXIST("CF2101", "系统配置错误：配置文件不存在"), 
	CONFIG_BLOCKCHAIN_USER_INCOMPLETE("CF2102", "系统配置错误：区块链系统用户信息不完整"),
	CONFIG_BLOCKCHAIN_NODE_INCOMPLETE("CF2103", "系统配置错误：区块链系统节点信息不完整"),
	CONFIG_BLOCKCHAIN_CHAINCODE_INCOMPLETE("CF2104", "系统配置错误：智能合约信息不完整"),
	CONFIG_BLOCKCHAIN_RETRY_INCOMPLETE("CF2105", "系统配置错误：发送重试信息不完整"),
	
	/** 数据库访问异常，格式为 "CF22XX"，以 DB 开头 */
	DATABASE_ERROR("CF2201","数据库异常"),
	DATABASE_CONNECT_ERROR("CF2202","数据库访问错误：数据库连接失败"),
	DATABASE_INSERT_ERROR("CF2203", "数据库访问错误：插入失败"),
	
	/** 区块链节点异常，格式为 "CF23XX"，以 FABRIC 开头 */
	BLOCKCHAIN_CLIENT_INIT_ERROR("CF2301", "区块链系统错误：客户端初始化异常"),
	BLOCKCHAIN_CHAIN_INIT_ERROR("CF2302", "区块链系统错误：区块链初始化异常"),
	BLOCKCHAIN_LISTENER_INIT_ERROR("CF2303", "区块链系统错误：区块链监听初始化异常"),
	BLOCKCHAIN_TRANSACTION_SEND_ERROR("CF2304", "区块链系统错误：交易发送异常"),
	BLOCKCHAIN_CHAINCODE_INSTALLATION_ERROR("CF2305", "区块链系统错误：智能合约安装异常"),
	BLOCKCHAIN_CHAINCODE_INSTANTIATE_ERROR("CF2306", "区块链系统错误：智能合约实例化异常"),
	BLOCKCHAIN_CHAINCODE_UPGRADE_ERROR("CF2307", "区块链系统错误：智能合约升级异常"),
	BLOCKCHAIN_BLOCKCHAIN_QUERY_ERROR("CF2308", "区块链系统错误：区块链查询异常"),
	BLOCKCHAIN_EVENTHUB_CONNECTION_ERROR("CF2309", "区块链系统错误：区块链监听连接异常"),
	
	/** ACTIVEMQ 相关异常，格式为 "CF24XX"，以 MQ 开头 */
	MQ_TOPIC_INVALID("CF2401", "消息系统错误：话题名称无效"), 
	MQ_CONNECT_FAILURE("CF2402", "消息系统错误：MQ连接失败"),
	
	;

	private String errorCode;
	private String errorMsg;

	ExceptionEnum(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
    
    
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

