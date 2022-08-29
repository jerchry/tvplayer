/*#######################################################
 *
 *   Maintained by Gregor Santner, 2019-
 *   https://gsantner.net/
 *
 *   License: Apache 2.0 / Commercial
 *  https://github.com/gsantner/opoc/#licensing
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
#########################################################*/

/*
 * Simple Parser for M3U playlists with some extensions
 * Mainly for playlists with video streams
 */
package com.jerry.tvplayer.m3u;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Parser for M3U playlists
 */
@SuppressWarnings({"WeakerAccess", "CaughtExceptionImmediatelyRethrown", "unused", "SpellCheckingInspection", "TryFinallyCanBeTryWithResources"})
public class M3UParser {
    private static final String TAG = "SimpleM3UParser";
    private final static String EXTINF_TAG = "#EXTINF:";
    private final static String EXTINF_TVG_NAME = "tvg-name=\"";
    private final static String EXTINF_TVG_ID = "tvg-id=\"";
    private final static String EXTINF_STATUS = "status=\"";
    private final static String EXTINF_TVG_LOGO = "tvg-logo=\"";
    private final static String EXTINF_TVG_EPGURL = "tvg-epgurl=\"";
    private final static String EXTINF_GROUP_TITLE = "group-title=\"";
    private final static String EXTINF_RADIO = "radio=\"";
    private final static String EXTINF_TAGS = "tags=\"";

    // ########################
    // ##
    // ## Members
    // ##
    // ########################
    private ArrayList<M3U_Entry> _entries;
    private M3U_Entry _lastEntry;

    // Parse m3u file by reading content from file by filepath
    public ArrayList<M3U_Entry> parse(String filepath) throws IOException {
        return parse(new FileInputStream(filepath));
    }

    // Parse m3u file by reading from inputstream
    public ArrayList<M3U_Entry> parse(InputStream inputStream) throws IOException {
        _entries = new ArrayList<>();
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                try {
                    parseLine(line);
                } catch (Exception e) {
                    _lastEntry = null;
                    e.printStackTrace();
                }
            }
        } catch (IOException rethrow) {
            rethrow.printStackTrace();
            throw rethrow;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return _entries;
    }

    // Parse one line of m3u
    private void parseLine(String line) {
        Log.e(TAG, "parseLine: " + line);
        line = line.trim();

        // EXTINF line
        if (line.startsWith(EXTINF_TAG)) {
            _lastEntry = parseExtInf(line);
        }
        // URL line (no comment, no empty line(trimmed))
        else if (!line.isEmpty() && !line.startsWith("#")) {
            if (_lastEntry == null) {
                _lastEntry = new M3U_Entry();
            }
            _lastEntry.setUrl(line);
            _entries.add(_lastEntry);
            _lastEntry = null;
        }
        // No useable data -> reset last EXTINF for next entry
        else {
            _lastEntry = null;
        }
    }

    private M3U_Entry parseExtInf(String line) {
        M3U_Entry curEntry = new M3U_Entry();
        StringBuilder buf = new StringBuilder(20);
        if (line.length() < EXTINF_TAG.length() + 1) {
            return curEntry;
        }

        // Strip tag
        line = line.substring(EXTINF_TAG.length());

        // Read seconds (may end with comma or whitespace)
        while (line.length() > 0) {
            char c = line.charAt(0);
            if (Character.isDigit(c) || c == '-' || c == '+') {
                buf.append(c);
                line = line.substring(1);
            } else {
                break;
            }
        }
        if (buf.length() == 0 || line.isEmpty()) {
            return curEntry;
        }
        curEntry.setSeconds(Integer.valueOf(buf.toString()));

        // tvg tags
        while (!line.isEmpty() && !line.startsWith(",")) {
            line = line.trim();
            if (line.startsWith(EXTINF_TVG_NAME) && line.length() > EXTINF_TVG_NAME.length()) {
                line = line.substring(EXTINF_TVG_NAME.length());
                int i = line.indexOf("\"");
                curEntry.setTvgName(line.substring(0, i));
                line = line.substring(i + 1);
            }
            if (line.startsWith(EXTINF_TVG_LOGO) && line.length() > EXTINF_TVG_LOGO.length()) {
                line = line.substring(EXTINF_TVG_LOGO.length());
                int i = line.indexOf("\"");
                curEntry.setTvgLogo(line.substring(0, i));
                line = line.substring(i + 1);
            }
            if (line.startsWith(EXTINF_TVG_EPGURL) && line.length() > EXTINF_TVG_EPGURL.length()) {
                line = line.substring(EXTINF_TVG_EPGURL.length());
                int i = line.indexOf("\"");
                curEntry.setTvgEpgUrl(line.substring(0, i));
                line = line.substring(i + 1);
            }
            if (line.startsWith(EXTINF_RADIO) && line.length() > EXTINF_RADIO.length()) {
                line = line.substring(EXTINF_RADIO.length());
                int i = line.indexOf("\"");
                curEntry.setIsRadio(Boolean.parseBoolean(line.substring(0, i)));
                line = line.substring(i + 1);
            }
            if (line.startsWith(EXTINF_GROUP_TITLE) && line.length() > EXTINF_GROUP_TITLE.length()) {
                line = line.substring(EXTINF_GROUP_TITLE.length());
                int i = line.indexOf("\"");
                curEntry.setGroupTitle(line.substring(0, i));
                line = line.substring(i + 1);
            }
            if (line.startsWith(EXTINF_TVG_ID) && line.length() > EXTINF_TVG_ID.length()) {
                line = line.substring(EXTINF_TVG_ID.length());
                int i = line.indexOf("\"");
                curEntry.setTvgId(line.substring(0, i));
                line = line.substring(i + 1);
            }
            if (line.startsWith(EXTINF_STATUS) && line.length() > EXTINF_STATUS.length()) {
                line = line.substring(EXTINF_STATUS.length());
                int i = line.indexOf("\"");
                curEntry.setStatus(line.substring(0, i));
                line = line.substring(i + 1);
            }
            if (line.startsWith(EXTINF_TAGS) && line.length() > EXTINF_TAGS.length()) {
                line = line.substring(EXTINF_TAGS.length());
                int i = line.indexOf("\"");
                curEntry.setTags(line.substring(0, i).split(","));
                line = line.substring(i + 1);
            }
        }

        // Name
        line = line.trim();
        if (line.length() > 1 && line.startsWith(",")) {
            line = line.substring(1);
            line = line.trim();
            if (!line.isEmpty()) {
                curEntry.setName(line);
            }
        }
        return curEntry;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////
    ///  Examples
    //
    public static class Examples {
        public static List<M3U_Entry> example() {
            try {
                M3UParser m3UParser = new M3UParser();
                File moviesFolder = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_MOVIES);
                return m3UParser.parse(new File(moviesFolder, "streams.m3u").getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        public static List<M3U_Entry> exampleWithLogoRewrite() {
            List<M3U_Entry> playlist = new ArrayList<>();
            try {
                M3UParser m3UParser = new M3UParser();
                File moviesFolder = new File(new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_MOVIES), "liveStreams");
                File logosFolder = new File(moviesFolder, "Senderlogos");
                File streams = new File(moviesFolder, "streams.m3u");
                for (M3U_Entry entry : m3UParser.parse(streams.getAbsolutePath())) {
                    if (entry.getTvgLogo() != null) {
                        String logo = new File(logosFolder, entry.getTvgLogo()).getAbsolutePath();
                        entry.setTvgLogo(logo);
                    }
                    playlist.add(entry);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return playlist;
        }

        public static void startStreamPlaybackInVLC(Activity activity, String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setDataAndTypeAndNormalize(Uri.parse(url), "video/*");
            intent.setPackage("org.videolan.vlc");
            activity.startActivity(intent);
        }
    }
}
