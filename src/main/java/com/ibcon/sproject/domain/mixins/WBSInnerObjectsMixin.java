package com.ibcon.sproject.domain.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"activities", "project", "estimateSmets"})
public class WBSInnerObjectsMixin {
}
