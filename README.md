xposed-whatsapp
===========

Utilising the [Xposed framework](http://repo.xposed.info/module/de.robv.android.xposed.installer), this module adds tweaks to make WhatsApp more useful to me. Currently includes:

- [Removal of the voice and/or camera buttons](#removal-of-the-camera-andor-voice-buttons) from the conversation view
- The ability to [keep the contact list](#keep-contact-list-in-the-history) (called "New Chat" in WhatsApp) in your history, so when you launch a conversation using it and then press the back button, it doesn't skip straight back to the main conversations list.
- [Highlight groups](#highlight-groups) in the conversations list

**Languages**:
- English
- Polish
- Turkish
- German
- Arabic

**Credits**:
- Removal of camera/voice buttons by [jaysh](github.com/jaysh/xposed-whatsapp)
- Keep contact list in history by [jaysh](github.com/jaysh/xposed-whatsapp)
- Icon by [Kucharskov](http://forum.xda-developers.com/member.php?u=4493226) ([GitHub](https://github.com/Kucharskov))
- Turkish translation by [King ov Hell](http://forum.xda-developers.com/member.php?u=5025244)
- Polish translation (+ integration of Turkish) by [Kucharskov](http://forum.xda-developers.com/member.php?u=4493226) ([GitHub](https://github.com/Kucharskov))
- German translation by [mihahn](http://forum.xda-developers.com/member.php?u=4660165) ([GitHub](https://github.com/Mihahn))
- Arabic translation by [Salim.Keady](http://forum.xda-developers.com/member.php?u=5477058)

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

Highlight Groups
----------------

It isn't always easy to see at a glance which of your conversations are groups, and which are which other people. This mod allows you to highlight the group conversations in any colour of your choice.

![Highlight Groups Example](https://raw.githubusercontent.com/jaysh/xposed-whatsapp/master/documentation/images/highlight-groups.png)

### Installation

To install, please ensure you first have:

1. Rooted your device
2. Installed the [Xposed Framework](http://repo.xposed.info/module/de.robv.android.xposed.installer)

Then, you can just search for "[WhatsApp Mods](repo.xposed.info/module/sh.jay.xposed.whatsapp)" on the store to install it.

### Configuration

Here are all of the options you currently get to play with:

![Preferences](https://raw.githubusercontent.com/jaysh/xposed-whatsapp/master/documentation/images/preferences.png)

### Troubleshooting & Support

If it feels like the module is doing nothing for you, please try enabling debugging mode from the settings, and send me the logs via the support thread: http://forum.xda-developers.com/xposed/modules/mod-whatsapp-mods-remove-action-t2824732 (either reply to the thread or send me a private message).

# FAQs

## What tools did you use to make this possible?

* Apktool 2 - to view Smali/Java, and step through code in `jdb`).
* JD-GUI - to convert Smali code into Java (mostly worthless, but nice for a general feel).
* `adb shell dumpsys activity` to see what activity is in the foreground, and inspect the history stack.
* `jdb` - to step through Smali, set breakpoints, and inspect variables
* [Hierarchy Viewer](http://developer.android.com/tools/help/hierarchy-viewer.html) to inspect layouts
