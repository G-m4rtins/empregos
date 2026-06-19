package com.gabriel.empregos.core.exceptions;

public class SkillNotFoundException extends ModelNotFoundException {

    public SkillNotFoundException() {
        super("Skill not found");
    }

        public SkillNotFoundException(String message) {
        super(message);
    }

}
