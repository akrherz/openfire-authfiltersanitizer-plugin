# Openfire AuthFilter Sanitizer Plugin

A plugin for the Openfire Real-time communications server that removes entries for Openfire's authentication filter that are susceptible to abuse (CVE-2023-32315).

## Installation
Copy the `authfiltersanitizer.jar` file into the plugins directory of your Openfire installation. The plugin will then be automatically deployed. To upgrade to a new version, copy the new `authfiltersanitizer.jar` file over the existing file.

## Configuration

The plugin can be configured by using these Openfire system properties:

- `plugin.authfiltersanitizer.sanitizetask.periodms` (default `10000` - The time (in milliseconds) between successive runs of the task that removes susceptible entries.
- `plugin.authfiltersanitizer.sanitizetask.delayms` (default `2000`) - The time (in milliseconds) between a (re)load of this plugin, and the first run of the task that removes susceptible entries.
- `plugin.authfiltersanitizer.sanitizetask.disableSearch`(default `false`) - Controls if, apart from known, hard-coded susceptible entries, the task will dynamically search for other susceptible entries.

## Using the Plugin
After the plugin has been installed, the Openfire authentication filter will be periodically cleaned from entries automatically.
