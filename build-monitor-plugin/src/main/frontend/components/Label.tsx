import React from "react";
import {useEffect, useState} from "react";

function Label({text}: {text: string}) {
    const [oldText, setOldText] = useState(text);
    const [animating, setAnimating] = useState(false);

    useEffect(() => {
        setTimeout(() => {
            if (oldText !== text) {
                setAnimating(true);
                setTimeout(() => {
                    setOldText(text);
                    setAnimating(false)
                }, 400);
            }
        }, 200);
    }, [text])

    return (
        <div className={'shineofthesun'} style={{position: 'relative'}}>
            <span className={"lablething1 " + (animating ? 'lablething1animating' : '')}>{oldText}</span>
            <span className={"lablething2 " + (animating ? 'lablething2animating' : '')}>{text}</span>
        </div>
    )
}

export default Label
