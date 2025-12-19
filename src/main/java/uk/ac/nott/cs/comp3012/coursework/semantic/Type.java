package uk.ac.nott.cs.comp3012.coursework.semantic;

import java.util.List;

public record Type(BaseType baseType, List<Integer> arrayDimensions, int pointerLevel) {
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Type)) {
            return false;
        }

        Type other = (Type) obj;

        if (arrayDimensions != null && other.arrayDimensions != null) {
            if (!arrayDimensions.equals(other.arrayDimensions)) {
                return false;
            }
        }

        if (arrayDimensions == null && other.arrayDimensions != null) {
            return false;
        }

        if (arrayDimensions != null && other.arrayDimensions == null) {
            return false;
        }

        return baseType == other.baseType && pointerLevel == other.pointerLevel;
    }
}

