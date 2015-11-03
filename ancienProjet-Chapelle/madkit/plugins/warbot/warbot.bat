echo off
java -cp ../../lib/boot.jar -Xms16m -Xmx128m madkit.boot.Madkit madkit.kernel.Booter --graphics --config warbot.cfg
		