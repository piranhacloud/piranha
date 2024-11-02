/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package temperature;

public class Temperature {

    /**
     * Defines the temperature scale enum.
     */
    public enum TemperatureScale {
        /**
         * Celsius constant.
         */
        CELSIUS,
        /**
         * Fahrenheit constant.
         */
        FAHRENHEIT
    }

    /**
     * Stores the temperature scale.
     */
    private TemperatureScale scale;

    /**
     * Stores the temperature.
     */
    private double temperature;

    /**
     * Get the temperature scale.
     * 
     * @return the temperature scale.
     */
    public TemperatureScale getScale() {
        return scale;
    }

    /**
     * Get the temperature.
     * 
     * @return the temperature.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Set the temperature scale.
     * 
     * @param scale the temperature scale.
     */
    public void setScale(TemperatureScale scale) {
        this.scale = scale;
    }

    /**
     * Set the temperature.
     * 
     * @param temperature the temperature.
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
