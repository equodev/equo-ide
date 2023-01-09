package dev.equo.solstice.p2;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class P2Model {
    private TreeSet<String> p2repo = new TreeSet<>();
    private TreeSet<String> install = new TreeSet<>();
    private TreeMap<String, Filter> filters = new TreeMap<>();

    public TreeSet<String> getP2repo() {
        return p2repo;
    }

    public TreeSet<String> getInstall() {
        return install;
    }

    public TreeMap<String, Filter> getFilters() {
        return filters;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('{');
        if (!p2repo.isEmpty()) {
            appendSet(buf, "p2repo", p2repo);
        }
        if (!install.isEmpty()) {
            appendSet(buf, "install", install);
        }
        if (!filters.isEmpty()) {
            String START___ = "filters: { '";
            String START = ",\n            '";
            boolean isFirst = true;
            for (var entry : filters.entrySet()) {
                String lead;
                if (isFirst) {
                    lead = START___;
                    isFirst = false;
                } else {
                    lead = START;
                }
                buf.append(lead + entry.getKey() + "': " + entry.getValue());
            }
            buf.append(" },\n");
        }
        return closeJson(buf);
    }

    public static class Filter {
        private final TreeSet<String> exclude = new TreeSet<>();
        private final TreeSet<String> excludePrefix = new TreeSet<>();
        private final TreeSet<String> excludeSuffix = new TreeSet<>();
        private final TreeMap<String, String> props = new TreeMap<>();

        public TreeSet<String> getExclude() {
            return exclude;
        }

        public TreeSet<String> getExcludePrefix() {
            return excludePrefix;
        }

        public TreeSet<String> getExcludeSuffix() {
            return excludeSuffix;
        }

        public TreeMap<String, String> getProps() {
            return props;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append('{');
            if (!exclude.isEmpty()) {
                appendSet(buf, "exclude", exclude);
            }
            if (!excludePrefix.isEmpty()) {
                appendSet(buf, "excludePrefix", excludePrefix);
            }
            if (!excludeSuffix.isEmpty()) {
                appendSet(buf, "excludeSuffix", excludeSuffix);
            }
            if (!props.isEmpty()) {
                String START___ = "props: { '";
                String START = ",\n          '";
                boolean isFirst = true;
                for (var entry : props.entrySet()) {
                    String lead;
                    if (isFirst) {
                        lead = START___;
                        isFirst = false;
                    } else {
                        lead = START;
                    }
                    buf.append(lead + entry.getKey() + "': '" + entry.getValue() + "'");
                }
                buf.append(" },\n");
            }
            return closeJson(buf);
        }
    }

    private static void appendSet(StringBuilder buf, String name, Set<String> toAdd) {
        buf.append(name);
        buf.append(": ['");
        buf.append(String.join("', '", toAdd));
        buf.append("'],\n");
    }

    private static String closeJson(StringBuilder buf) {
        if (buf.charAt(buf.length() - 1) == '\n') {
            buf.setLength(buf.length() - 1);
            buf.setCharAt(buf.length() - 1, '}');
        } else {
            buf.append('}');
        }
        return buf.toString();
    }
}
