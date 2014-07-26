xposed-whatsapp
===========

Utilising the [Xposed framework](http://repo.xposed.info/module/de.robv.android.xposed.installer), this module adds tweaks to make WhatsApp more useful to me. Currently, it allows you to hide the camera and/or voice message shortcuts (independently), but that could change as I discover more changes I'd like to see in WhatsApp that they are unlikely to implement.

**Languages**:
- English
- Polish
- Turkish

**Credits**:
- Removal of camera/voice buttons by [jaysh](github.com/jaysh/xposed-whatsapp)
- Keep contact list in history by [jaysh](github.com/jaysh/xposed-whatsapp)
- Icon by [Kucharskov](http://forum.xda-developers.com/member.php?u=4493226) ([GitHub](https://github.com/Kucharskov))
- Turkish translation by [King ov Hell](http://forum.xda-developers.com/member.php?u=5025244)
- Polish translation (+ integration of Turkish) by [Kucharskov](http://forum.xda-developers.com/member.php?u=4493226) ([GitHub](https://github.com/Kucharskov))

My extended thanks to the contributors.

Features available are detailed in the different sections.

Removal of the camera and/or voice buttons
------------------------------------------

Some time ago, WhatsApp added the "audio voice" feature. I found it hindered significantly more than it helped: if you tapped it accidentally, it would vibrate and emit a notification-like sound. And, I would do this repeatedly and so never found this useful (I don't use voice messaging).

More time passes, and they add a camera icon. This is more useful, and personally I'd prefer if that was the only one. So - I wrote this.

#### Before (with both camera and voice visible):
![Before](https://raw.githubusercontent.com/jaysh/xposed-whatsapp/master/documentation/images/before.png)

#### After (no voice, no camera):
![After](https://raw.githubusercontent.com/jaysh/xposed-whatsapp/master/documentation/images/after-no-camera-no-voice.png)

#### After (no voice):
![After](https://raw.githubusercontent.com/jaysh/xposed-whatsapp/master/documentation/images/after-no-voice.png)

#### After (no camera):
![After](https://raw.githubusercontent.com/jaysh/xposed-whatsapp/master/documentation/images/after-no-camera.png)

(*Note:* in all cases, the "send message" button will appear after you start typing, as it always would. Additionally, disabling sending still allows you to receive them.)

I have tested it on my HTC One M7, using Android Revolution HD, but I have no reason to suspect it shouldn't work on other devices.

Keep contact list in the history
--------------------------------

When navigating to a conversation via the "contact list" (main WhatsApp screen > "New Chat" on the action bar > Select a contact) and you press the back key, rather than returning to the contact list, it returns you back to the main WhatsApp screen with your list of conversations - which can be a little frustrating.

This mod allows you to "go back" to the contact list, so you can chat uninterrupted.

### Troubleshooting

If it feels like the module is doing nothing for you, please try enabling debugging mode from the settings, and send me the logs via the support thread: http://forum.xda-developers.com/xposed/modules/mod-whatsapp-mods-remove-action-t2824732