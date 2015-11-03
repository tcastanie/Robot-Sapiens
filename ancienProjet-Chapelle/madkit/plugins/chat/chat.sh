#!/bin/bash
java -cp ../../lib/boot.jar -Xms16m -Xmx128m madkit.boot.Madkit madkit.kernel.Booter --graphics --config chat.cfg
		