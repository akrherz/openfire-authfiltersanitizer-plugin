/**
 * Copyright (C) 2023 Ignite Realtime Foundation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.igniterealtime.openfire.plugin;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.TaskEngine;

import java.io.File;
import java.util.TimerTask;

/**
 * An Openfire plugin that removes entries for Openfire's authentication filter that are susceptible to abuse.
 *
 * @author Guus der Kinderen, guus.der.kinderen@gmail.com
 */
public class AuthFilterSanitizerPlugin implements Plugin
{
    private TimerTask sanitizeTask;

    public void initializePlugin(PluginManager manager, File pluginDirectory)
    {
        // Deliberately use the older JiveGlobals API, and not the SystemProperties API, to allow for backwards compatibility.
        final long periodMs = JiveGlobals.getLongProperty("plugin.authfiltersanitizer.sanitizetask.periodms", 10000);
        final long delayMs  = JiveGlobals.getLongProperty("plugin.authfiltersanitizer.sanitizetask.delayms", 2000);

        sanitizeTask = new SanitizeTask();
        TaskEngine.getInstance().schedule(sanitizeTask, delayMs, periodMs);
    }

    public void destroyPlugin()
    {
        if (sanitizeTask != null) {
            TaskEngine.getInstance().cancelScheduledTask(sanitizeTask);
            sanitizeTask = null;
        }
    }
}
