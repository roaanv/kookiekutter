APP_VERSION:=1.0-SNAPSHOT
APP_JAR:=kookiekutter-$(APP_VERSION).jar
JAR_DIR:=target

package:
	mvn package

all: build

build: package

run:
	java -jar $(JAR_DIR)/$(APP_JAR)

