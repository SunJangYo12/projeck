#!/bin/sh
spinner()

# Animation
function spinner {

        pid=$1
        delay=0.15
        spinstr='|/-\'
                while [ "$(ps a | awk '{print $1}' | grep $pid)" ]; do
                        local temp=${spinstr#?}
                        printf " [%c]  " "$spinstr"
                        local spinstr=$temp${spinstr%"$temp"}
                        sleep $delay
                        printf "\b\b\b\b\b\b"
                done
        printf "    \b\b\b\b"
}
