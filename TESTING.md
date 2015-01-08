Testing Checklist
=================

Because there aren't (yet) any automated tests, this is a checklist to use whenever producing a release build.

These tests are performed assuming a fresh installation, and the WhatsApp installation having at least 2 conversations: 1 which is with another person, and 1 with a group. If they are not being conducted under these conditions, then it is important to pay attention to the settings currently applied in order to deduce whether a feature is actually working correctly or not. It is strongly advised to uninstall, reboot and then install before each release.

Sanity tests
============

- Removal of the camera and/or voice buttons
    1. Open any conversation
    2. In the footer, there should be no:
        - Camera icon
        - Voice icon
    3. When you start typing a message, the send icon should appear
- Highlight groups feature
    1. Open conversations list
    2. For any groups listed, they should have a grey background
- Keep contact list in the history
    1. Open conversations list
    2. Click "New Chat"
    3. Pick any contact
    4. Press the "back" key
    5. The "New Chat" view should be visible (*not* the conversations list)

# Feature testing

Coming soon :-)

# Regression tests

1. BUG#6
    - From "New Chat", if you click a contact, then forward a message, going back should still return you to the "New Chat" view and not the main conversations list.