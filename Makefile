GRAALVM = $(HOME)/graalvm-ce-java11-20.2.0-dev
# works graalvm-ce-java11-19.3.1
SRC = src/locking_test/core.clj
VERSION = 0.1.0-SNAPSHOT

all: build/locking_test run

clean:
	-rm -rf build target
	lein clean

target/uberjar/locking_test-$(VERSION)-standalone.jar: $(SRC)
	GRAALVM_HOME=$(GRAALVM) lein uberjar

uberjar: target/uberjar/locking_test-$(VERSION)-standalone.jar

build/locking_test: target/uberjar/locking_test-$(VERSION)-standalone.jar
	-mkdir build
	export
	$(GRAALVM)/bin/native-image \
		-jar target/uberjar/locking_test-$(VERSION)-standalone.jar \
		-H:Name=build/locking_test \
		-H:+ReportExceptionStackTraces \
		-J-Dclojure.spec.skip-macros=true \
		-J-Dclojure.compiler.direct-linking=true \
		-H:ConfigurationFileDirectories=graal-configs/ \
		--initialize-at-build-time \
		-H:Log=registerResource: \
		-H:EnableURLProtocols=http,https \
		--verbose \
		--allow-incomplete-classpath \
		--no-fallback \
		--no-server \
		"-J-Xmx4g"

run: build/locking_test
	build/locking_test
