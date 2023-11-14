ifdef OS
	APP_DIST = build\install\app\bin\app
	GRADLEW_DIST = gradlew
else
	ifeq ($(shell uname), Linux)
	APP_DIST = ./build/install/app/bin/app
	GRADLEW_DIST =./gradlew
	endif
endif

.DEFAULT_GOAL := build-run

clean:
	$(GRADLEW_DIST) clean

build:
	$(GRADLEW_DIST) clean build

install:
	$(GRADLEW_DIST) clean install

run-dist:
	$(APP_DIST)

run:
	$(GRADLEW_DIST) --console plain jshell

test:
	$(GRADLEW_DIST) test

report:
	$(GRADLEW_DIST) jacocoTestReport

lint:
	$(GRADLEW_DIST) checkstyleMain checkstyleTest

update-deps:
	$(GRADLEW_DIST) useLatestVersions

mk-gradlew-exec:
	git update-index --chmod=+x gradlew
	git commit -m "Make gradlew executable"
	git push


build-run: build run

.PHONY: build
