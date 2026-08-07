---
title: "Hacker News, GitHub and AI"
date: 2026-07-30T20:00:00+01:00
draft: false
---

Keeping up with recent technology developments was already hard before AI. Now that LLMs can be used to produce more stuff even faster, the perceived amount of new tools and libraries that are published every day feels staggering.

I like to read Hacker News every now and then to get a rough overview of what is happening and what might be worth looking into. My subjective feeling was that around October/November last year an inflection point was reached and the amount of new GitHub repos started to rise sharply, with a significant slowdown during the last weeks.

In an attempt to make my subjective feeling more objective, in this post I will analyze the content posted on Hacker News in this regard, using posted GitHub repositories as a stand-in for the amount of newly written software. In the second part, I will analyze a subset of those posted GitHub repositories regarding metrics that might be influenced by AI usage.


For this analysis I fetched all stories since March 2024 that had a GitHub repo as URL. The very first graph, which shows the amount of repos posted every day, already confirms my suspicion that around October/November 2025 the amount of repositories posted started rising, reaching a maximum in Q1 2026. Since the peak it has substantially leveled off, but still resides on a noticeably higher level compared to the previous year.

![GitHub repos posted](/img/hackernews-github-ai/posts.png)

The next graphs show the interactions those stories triggered, in terms of score of the story and amount of comments. Those mostly follow the patterns from before October/November 2025: most stories do not create much buzz, but the general rate of interactions roughly stayed the same. I added an extra 7-day average for stories with zero comments, because I had the suspicion that more repos might get proportionally more ignored, but it just shows that now there is simply more of everything.

![Story Engagement](/img/hackernews-github-ai/engagement.png)


Now the interesting part is to look into the repositories themselves. Because cloning ~45,000 repositories was not feasible for me, I only selected the ones with at least 10 comments or a score of 10 or greater, which reduced the total number to clone to ~4,500.

Looking at those, I tried to categorize the commits into AI-assisted and non-AI-assisted commits by looking for the typical AI identifiers in the co-authored tags. Here a general rise in commits is visible that starts way after the peak of posted GitHub repositories. And while the amount of commits that are marked as AI-assisted also rises, my assumption is that a big chunk of the unmarked commits is also AI-assisted, which I derive from the sheer volume.

![Commits](/img/hackernews-github-ai/commits.png)


This is also underpinned by the next graph, which shows changed lines per commit, which have also substantially increased for non-AI-marked commits.

![Changes](/img/hackernews-github-ai/changes.png)

As someone who is and has been responsible for the long-term maintenance of IT systems, the big question is how all this will pan out in the future. How can this amount of code be tamed and maintained in the coming years? My suspicion for the posted repositories is that a lot of them are AI-based one-shots, and only a fraction of them will enter long-term maintenance. I tried to visualize this by using the time between the first and most recent commit as an indication of how long a repository has been alive.
This is currently inconclusive, because most of the recently created repositories have not had the time yet to develop a history. There is, however, a slight trend visible that over time fewer and fewer older repositories with a long timespan are posted.

![Commit Spans](/img/hackernews-github-ai/commit_span.png)


Finally, I computed a pulse for each repo: a repo with at least one commit every day has a pulse of 100, a repo where only half of the days see a commit has a pulse of 50, and so on. Here we can see that more recently posted repos show more activity (as one might expect). 

![Commit Spans](/img/hackernews-github-ai/pulse.png)


It still seems to be a bit early to tell how the long-term viability of the huge amount of new repos still will pan out. To get real numbers on how those AI-assisted repositories evolve over time, more time needs to pass, so I will repeat this exercise at the end of the year.