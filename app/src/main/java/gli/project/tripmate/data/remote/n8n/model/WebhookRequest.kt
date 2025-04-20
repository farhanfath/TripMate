package gli.project.tripmate.data.remote.n8n.model

import gli.project.tripmate.domain.model.n8n.type.InputType

data class WebhookRequest(
    val id: String,
    val message: String,
    val inputType: InputType
)