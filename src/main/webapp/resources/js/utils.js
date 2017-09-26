function getDisplayableTime(now, delta)
{
    var difference;
    var mDate = now;

    if(mDate > delta)
    {
        difference = mDate - delta;
        var seconds = (difference/1000);
        var minutes = (seconds/60);
        var hours = (minutes/60);
        var days = (hours/24);
        var months = (days/31);
        var years = (days/365);

        if (seconds < 0)
        {
            return "not yet";
        }
        else if (seconds < 60)
        {
            return seconds == 1 ? "one second ago" : seconds + " seconds ago";
        }
        else if (seconds < 120)
        {
            return "a minute ago";
        }
        else if (seconds < 2700) // 45 * 60
        {
            return minutes + " minutes ago";
        }
        else if (seconds < 5400) // 90 * 60
        {
            return "an hour ago";
        }
        else if (seconds < 86400) // 24 * 60 * 60
        {
            return hours + " hours ago";
        }
        else if (seconds < 172800) // 48 * 60 * 60
        {
            return "yesterday";
        }
        else if (seconds < 2592000) // 30 * 24 * 60 * 60
        {
            return days + " days ago";
        }
        else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
        {
            return months <= 1 ? "one month ago" : days + " months ago";
        }
        else
        {
            return years <= 1 ? "one year ago" : years + " years ago";
        }
    }
    return "";
}

function getDisplayableTimeInterval(mins) {
    if (mins < 60) {
        return mins + " m";
    }

    if (mins >= 60 && mins < 60 * 24) {
        return Math.floor(mins / 60) + " h : " + mins % 60 + " m";
    }

    if (mins >= (60 * 24)) {
        return Math.floor(mins/24/60) + "d : " + Math.floor(mins/60%24) + 'h : ' + mins%60 + "m";
    }
}

function getPersentFromMillis(millis, startTimeStamp, endTimeStamp) {
    return Math.round(millis * 100 / (endTimeStamp - startTimeStamp));
}

function getTimeRatio(millis, startTimeStamp, endTimeStamp) {
    var activeHours = Math.round(millis / 3600000);
    var totalHours = Math.round((endTimeStamp - startTimeStamp) / 3600000);
    return activeHours + "h / " + totalHours + "h";
}