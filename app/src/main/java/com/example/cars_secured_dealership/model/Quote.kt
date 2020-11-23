package com.example.cars_secured_dealership.model

data class Quote(
    var quote: String
) {
    companion object{
        val QUOTE_NAMES = listOf(
            "I think that’s the single best piece of advice: constantly think about how you could be doing things better and questioning yourself",
            "Value the relationship more than making your quota.",
            "Approach each customer with the idea of helping him or her to solve a problem or achieve a goal, not of selling a product or service.",
            "If you are not taking care of your customer, your competitor will.",
            "Quality means doing it right when no one is looking.",
            "As sellers, if we are going to be successful landing the big ones, we have to expand our thinking about what’s possible.",
            "My definition of ‘innovative’ is providing value to the customer.",
            "It’s not the end of the world. It’s the beginning of a powerful, all-new world.",
            "I’ve learned the real keys to success in sales are: CLEAR GOALS – EDUCATION – SKILLS – DISCIPLINE – INTEGRITY.",
            "If you always do what you’ve always done, you’ll always get what you’ve always got.",
            "When billionaire car dealers or manufacturers pay for ambassadorships, at least they pay with money earned by selling something of value.",
        )
    }
}
