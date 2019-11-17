package com.etandon.jobcoin.api.routes.jsonschemas

import com.etandon.jobcoin.api.data.AssignAddressDTO

trait TemplateJsonSchema
  extends endpoints.algebra.JsonSchemaEntities with endpoints.generic.JsonSchemas {
  implicit lazy val assignAddressDTOJsonSchema: JsonSchema[AssignAddressDTO] =
    genericJsonSchema[AssignAddressDTO]
}
