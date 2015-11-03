#!/bin/bash
java -cp ../../lib/boot.jar -Xms16m -Xmx128m "-Dpython.home=lib" madkit.boot.Madkit madkit.desktop2.DesktopBooter --graphics  --config template.cfg
		