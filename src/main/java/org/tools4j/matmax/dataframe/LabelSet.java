package org.tools4j.matmax.dataframe;

import java.util.Arrays;
import java.util.Comparator;

public interface LabelSet {
    int size();
    String label(int index);
    Object labelAsObj(int index);
    int indexOf(String label);
    int indexOfObj(Object label);

    interface StringLabelSet extends LabelSet {
        @Override
        default String labelAsObj(final int index) {
            return label(index);
        }

        @Override
        default int indexOfObj(final Object label) {
            return indexOf(label == null ? null : label.toString());
        }

        static StringLabelSet create(final String... labels) {
            final int n = labels.length;
            final Object[][] labelsAndIndices = new Object[n][2];
            for (int i = 0; i < n; i++) {
                labelsAndIndices[i][0] = labels[i];
                labelsAndIndices[i][1] = i;
            }
            Arrays.sort(labelsAndIndices, Comparator.comparing(li -> li[0].toString()));
            final String[] sortedLabels = new String[n];
            final int[] indices = new int[n];
            for (int i = 0; i < n; i++) {
                sortedLabels[i] = labelsAndIndices[i][0].toString();
                indices[i] = (int)labelsAndIndices[i][1];
            }
            return new StringLabelSet() {
                @Override
                public int size() {
                    return labels.length;
                }

                @Override
                public String label(final int index) {
                    return labels[index];
                }

                @Override
                public int indexOf(final String label) {
                    final int index = Arrays.binarySearch(sortedLabels, label);
                    return index >= 0 ? index : -1;
                }
            };
        }
    }

    interface ObjLabelSet<V> extends LabelSet {
        @Override
        V labelAsObj(int index);
        @Override
        int indexOfObj(Object label);
        @Override
        default String label(final int index) {
            final V label = labelAsObj(index);
            return label == null ? null : label.toString();
        }
    }

    @FunctionalInterface
    interface IntLabelSet extends LabelSet {
        @Override
        default String label(final int index) {
            final Integer label = labelAsObj(index);
            return label == null ? null : label.toString();
        }

        @Override
        default Integer labelAsObj(final int index) {
            return (index >= 0 && index <= size()) ? index : null;
        }

        @Override
        default int indexOf(final String label) {
            try {
                final int index = Integer.parseInt(label);
                if (index >= 0 && index <= size()) return index;
            } catch (final Exception e) {
                //ignore
            }
            return -1;
        }

        @Override
        default int indexOfObj(final Object label) {
            if (label instanceof Integer) {
                final int index = (int)label;
                if (index >= 0 && index <= size()) return index;
            }
            return -1;
        }
    }
}
