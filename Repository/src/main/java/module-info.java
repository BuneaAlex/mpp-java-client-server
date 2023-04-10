module Repository.org.exampleR {
    requires org.apache.logging.log4j;
    requires java.sql;
    requires Model.org.exampleM;
    exports org.exampleR;
    exports org.exampleR.service;
}