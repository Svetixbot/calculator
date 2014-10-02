# Functional style calculator

## Install and download all the things

Install sbt

	$ brew install sbt

Install ide of choice ?

	[IntelliJIDEA 14 EAP](http://confluence.jetbrains.com/display/IDEADEV/IDEA+14+EAP)
		+ [install Scala plugin](http://confluence.jetbrains.com/display/SCA/Getting+Started+with+IntelliJ+IDEA+Scala+Plugin)

Run this thing

	$ sbt

Compile

	> compile

Make sure tests are green

	> test


## Develop

Compile

	> compile

Run tests

	> test

Run single test

	> test-only com.meow.meow.CatSpec

Run compile in a background

	> ~compile

Run tests in a background

	> ~test
	> ~test-only com.meow.meow.CatSpec

	
