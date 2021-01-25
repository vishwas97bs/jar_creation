package helper.gimletcore.constants;

public class OcrWebConstants
{
    public static final String WEB_PROP_FILE_NAME = "gimlet-web.properties";
    public static final String CORE_PROP_FILE_NAME = "gimlet-core.properties";
    public static final String KAFKA_PRODUCER_PROP_FILE_NAME = "gimlet-kafka-producer.properties";
    public static final String KAFKA_CONSUMER_PROP_FILE_NAME = "gimlet-kafka-consumer.properties";
    public static final String S3_ACCESS_KEY = "accessKey";
    public static final String S3_SECRET_KEY = "secretKey";
    public static final String S3_REGION = "region";
    public static final String S3_BUCKET_NAME = "bucketName";
    public static final String S3_BASE_URL = "s3BaseUrl";
    public static final String THUMB_IMG_HEIGHT = "imageHeight";
    public static final String THUMB_IMG_WIDTH = "imageWidth";
    public static final String OCR_IMAGE_FORMAT_JPG = "jpg";
    public static final String OCR_IMAGE_FORMAT_JPEG = "jpeg";
    public static final String SCAN_REQUEST_BASE_DIRECTORY = "data_dir";
    public static final String USE_LOCAL_FILE_STORAGE_FOR_PROCESSING = "use_local_file_storage";
    public static final String DEMO_PAGE = "demo.html";
    public static final String DEMO_PAGE_TOKEN = "DEMOPAGETOKEN";
    public static final String DEMO_PAGE_TOKEN_USER = "DEMOPAGETOKENUSER";

    // Redis properties
    public static final String REDIS_HOST = "redis.hostname";
    public static final String REDIS_PORT = "redis.port";
    public static final String REDIS_RECEIPT_CACHE_RETENTION_SECS = "redis.receipt.cache.retention.seconds";
    public static final String REDIS_INVOICE_CACHE_RETENTION_SECS = "redis.invoice.cache.retention.seconds";

    // Kafka properties
    public static final String SCANRESPONSE_TOPIC = "kafka.scanresponse.topic";
    public static final String KAFKA_BROKER_ADDRESS = "kafka.broker.address";

    public static final String IMAGE_SERVER_PROXY_GET = "image.server.proxyGET";


    private OcrWebConstants()
    {}
}
