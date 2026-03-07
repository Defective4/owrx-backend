package io.github.defective4.sdr.owrxsrc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class AssetCompiler {
    private static final Map<String, List<String>> ASSETS = Map.of("receiver.js",
            List.of("lib/chroma.min.js", "openwebrx.js", "lib/jquery-3.2.1.min.js", "lib/jquery.nanoscroller.min.js",
                    "lib/lame.min.js", "lib/Header.js", "lib/Demodulator.js", "lib/DemodulatorPanel.js",
                    "lib/BookmarkLocalStorage.js", "lib/BookmarkBar.js", "lib/BookmarkDialog.js", "lib/AudioEngine.js",
                    "lib/ProgressBar.js", "lib/Measurement.js", "lib/FrequencyDisplay.js", "lib/MessagePanel.js",
                    "lib/Js8Threads.js", "lib/Modes.js", "lib/MetaPanel.js", "lib/Waterfall.js", "lib/Shortcuts.js",
                    "lib/Bandplan.js", "lib/Spectrum.js", "lib/Scanner.js", "lib/Lookup.js", "lib/Utils.js",
                    "lib/Clock.js", "lib/Chat.js", "lib/UI.js"));
    private final Map<String, String> compiled = new HashMap<>();

    public AssetCompiler(String assetPath) throws IOException {
        for (Entry<String, List<String>> entry : ASSETS.entrySet()) {
            try (StringWriter buffer = new StringWriter()) {
                try (PrintWriter writer = new PrintWriter(buffer)) {
                    for (String res : entry.getValue()) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                                getClass().getResourceAsStream(assetPath + "/" + res), StandardCharsets.UTF_8))) {
                            while (true) {
                                String line = reader.readLine();
                                if (line == null) break;
                                writer.println(line);
                            }
                            writer.println();
                        }
                    }
                }
                compiled.put(entry.getKey(), buffer.toString());
            }
        }
    }

    public Optional<String> getCompiledAsset(String path) {
        return Optional.ofNullable(compiled.get(path));
    }
}
