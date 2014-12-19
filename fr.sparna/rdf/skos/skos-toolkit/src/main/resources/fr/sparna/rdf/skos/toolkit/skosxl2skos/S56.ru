# S56 : The property chain (skosxl:altLabel, skosxl:literalForm) is a sub-property of skos:altLabel.
PREFIX skos:<http://www.w3.org/2004/02/skos/core#>
PREFIX skosxl:<http://www.w3.org/2008/05/skos-xl#>
INSERT {
	?x skos:altLabel ?y
} WHERE {
	?x skosxl:altLabel/skosxl:literalForm ?y .
}