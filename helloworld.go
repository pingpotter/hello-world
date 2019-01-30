package main

import (
	"fmt"
	"os"
	"os/signal"
	"syscall"

	"github.com/bamzi/jobrunner"
)

func main() {

	osSignals := make(chan os.Signal, 1)
	signal.Notify(osSignals, os.Interrupt, syscall.SIGTERM)

	jobrunner.Start() // optional: jobrunner.Start(pool int, concurrent int) (10, 1)
	jobrunner.Schedule("@every 5s", ReminderEmails{})
	jobrunner.Schedule("@every 5s", ReminderEmails2{})

	<-osSignals
}

// Job Specific Functions
type ReminderEmails struct {
	// filtered
}

// ReminderEmails.Run() will get triggered automatically.
func (e ReminderEmails) Run() {
	// Queries the DB
	// Sends some email
	fmt.Printf("Every 5 sec send reminder emails \n")
}

// Job Specific Functions
type ReminderEmails2 struct {
	// filtered
}

// ReminderEmails.Run() will get triggered automatically.
func (e ReminderEmails2) Run() {
	// Queries the DB
	// Sends some email
	fmt.Printf("2 Every 5 sec send reminder emails \n")
}
