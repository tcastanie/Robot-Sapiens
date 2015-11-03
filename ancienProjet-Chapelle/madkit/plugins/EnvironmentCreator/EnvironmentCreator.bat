echo off
java -cp ../../lib/boot.jar -Xms16m -Xmx128m "-Dpython.home=lib" madkit.boot.Madkit madkit.kernel.Booter --graphics  --config EnvironmentCreator.cfg
		