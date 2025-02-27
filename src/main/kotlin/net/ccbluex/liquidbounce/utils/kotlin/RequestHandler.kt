/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2015 - 2024 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */
package net.ccbluex.liquidbounce.utils.kotlin

import net.ccbluex.liquidbounce.features.module.Module
import java.util.*
import kotlin.math.max

class RequestHandler<T> {
    private val activeRequests = PriorityQueue<Request<T>>(compareBy { -it.priority })

    fun tick(deltaTime: Int = 1) {
        this.activeRequests.forEach { it.expiresIn = max(it.expiresIn - deltaTime, 0) }
        this.activeRequests.removeIf { it.expiresIn <= 0 }
    }

    fun request(request: Request<T>) {
        // we remove all requests provided by module on new request
        activeRequests.removeAll { it.provider == request.provider }
        this.activeRequests.add(request)
    }

    fun getActiveRequestValue(): T? {
        if (this.activeRequests.isEmpty())
            return null

        return this.activeRequests.peek().value
    }

    fun removeActive() {
        if (this.activeRequests.isNotEmpty()) {
            this.activeRequests.remove(this.activeRequests.peek())
        }
    }

    /**
     * @param expiresIn in how many time units should this request expire?
     * @param priority higher = higher priority
     * @param provider module which requested value
     */
    class Request<T>(
        var expiresIn: Int,
        val priority: Int,
        val provider: Module,
        val value: T
    )
}
