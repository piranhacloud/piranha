
module cloud.piranha.test.server {
    exports cloud.piranha.test.server;
    opens cloud.piranha.test.server;
    requires java.net.http;
    requires org.junit.jupiter.api;
}
