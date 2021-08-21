# Stalked By The State

Preventative/proactive home and business security state machine

StalkedByTheState is a physical security system that employs convolutional neural network based computer vision for alert generation.

Designed to operate as a robust applicance that is installed in just a few commands, installed with this project:

https://github.com/hcfman/sbts-install

Currently it installs to the NVIDIA Jetson nano, NX and AGX.

StalkedByTheState supports PJ Reddie's YoloV3 and AlexeyAB's YoloV3 and YoloV4 out of the box. A mechanism is provided that continuously scans several cameras evaluating each image against a flexible alert definition. If it a hit occurs then an event is triggered that can sent a video alert (Amongst many other things).

In addition to providing video alerting and logging it also supports some limited local control. It supports the Phidget interfacekit and rfxtrx433 transceiver devices for hardware input and output and 433 Mhz device input and output control.

The whole system is installed with just one command. You can choose the Yolo algorithm by uncommented the one you wish.

This release is in preparation stage

