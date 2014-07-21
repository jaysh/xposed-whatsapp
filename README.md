xposed-whatsapp
===========

Utilising the [Xposed framework](http://repo.xposed.info/module/de.robv.android.xposed.installer), this module adds tweaks to make WhatsAp more useful to me. Currently, it only contains one tweak, but that could change as I discover more changes I'd like to see in WhatsApp.

Removal of the camera and voice buttons
---------------------------------------

Some time ago, WhatsApp added the "audio voice" feature. I found it hindered significantly more than it helped: if you tapped it accidentally, it would vibrate and emit a notification-like sound. And, I would do this repeatedly.

More time passes, and they add a camera icon. By this point, I felt I needed to take action.

Before:
After:

I have tested it on my HTC One M7, using Android Revolution HD, but I have no reason to suspect it shouldn't work on other devices.

### Troubleshooting

If it feels like the module is doing nothing for you, please try enabling debugging mode from the settings, and send me the logs.